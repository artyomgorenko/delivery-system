package ru.delivery.system.views.child_screens

import javafx.beans.property.*
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import ru.delivery.system.controllers.CargoListController
import ru.delivery.system.controllers.ProductListController
import ru.delivery.system.models.json.Cargo
import ru.delivery.system.models.json.OrderInfo
import ru.delivery.system.models.json.ProductListEntity
import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*
import java.time.LocalDate
import java.util.*

class ReportScreen : View("Отчётность") {


    override val root = tabpane {
        tab<TabOne>()
        tab<TabTwo>()
        tab<TabThree>()
    }

    /**
     * Стоимость товаров
     */
    class TabOne : View("Стоимость товаров") {

        private val cargoList = listOf(
            Cargo().apply {
                cargoId = 1
                productId = 1
                warehouseId = 1
                orderIdList = listOf(1)
                status = "NEW"
                movementsList = listOf(
                    Cargo.Movement().apply {
                        movementId = 1
                        newStatus = "DONE"
                        prevStatus = "OFFLOADED"
                        date = Date()
                    }
                )
            }
        ).observable()//CargoListController.cargoList.observable()

        private var cargoProperty = SimpleObjectProperty<Cargo>(Cargo())
        private var orferInfoListProperty: SimpleListProperty<OrderInfo> = SimpleListProperty()

        override fun onDock() {
            CargoListController.updateCargoList()
            super.onDock()
        }


        override val root = borderpane {
            var cost = SimpleIntegerProperty(10)
            top(ChildScreenHeader::class)

            center {
                vbox {
                    style {
                        backgroundColor += Color.WHITE
                        borderWidth += CssBox(
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px)
                        )
                    }

                    data class TempCargo(val cargoId: String, val articleId:String, val warehouseId:String, val cost:String)
                    val tempCargoList = listOf(
                        TempCargo("00001", "product #1", "#1", "741 р."),
                        TempCargo("00002", "product #1", "#1", "469 р."),
                        TempCargo("00003", "product #1", "#2", "124 р."),
                        TempCargo("00004", "product #1", "#2", "120 р."),
                        TempCargo("00005", "product #2", "#3", "316 р."),
                        TempCargo("00006", "product #2", "#5", "117 р."),
                        TempCargo("00007", "product #4", "#3", "543 р."),
                        TempCargo("00008", "product #2", "#3", "912 р.")
                    ).observable()

                    label("Список товаров") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    tableview(tempCargoList) {
                        minHeightProperty().set(800.0)
                        readonlyColumn("Номер товара", TempCargo::cargoId)
                        readonlyColumn("Номер артикула", TempCargo::articleId)
                        readonlyColumn("Номер склада", TempCargo::warehouseId)
                        readonlyColumn("Стоимость", TempCargo::cost)
                    }

                    // TODO: this is old version
//                    tableview(cargoList) {
//                        readonlyColumn("Номер товара", Cargo::cargoId)
//                        readonlyColumn("Номер артикула", Cargo::productId)
//                        readonlyColumn("Номер склада", Cargo::warehouseId)
//                        readonlyColumn("Стоимость", Cargo::cargoId)
//                        // TODO: no need in rowExpander
////                    rowExpander(expandOnDoubleClick = true) {
////                        paddingLeft = expanderColumn.width
////                        tableview(it.movementsList.observable()) {
////                            readonlyColumn("Id", Cargo.Movement::movementId)
////                            readonlyColumn("Старый статус", Cargo.Movement::prevStatus)
////                            readonlyColumn("Новый статус", Cargo.Movement::newStatus)
////                            readonlyColumn("Дата смены", Cargo.Movement::date)
////                        }
////                    }
//                        onSelectionChange { selectedCargo ->
//                            cargoProperty.value = selectedCargo
//                            orferInfoListProperty.value = OrderListController.allOrders.filter { cargoProperty.value.orderIdList.contains(it.orderId) }.observable()
//                        }
//                    }
                }
            }
            right {
                vbox {
                    minWidthProperty().set(500.0)
                    style {
                        backgroundColor += Color.WHITE
                        borderWidth += CssBox(
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px),
                            Dimension(1.0, Dimension.LinearUnits.px)
                        )
                    }

                    data class TempMovement(val dateInterval: String, val daysCount:String, val warehouseId:String, val cost:String)
                    val tempMovementList = listOf(
                        TempMovement("13.05.19 - 29.05.19", "1", "1", "184 р."),
                        TempMovement("29.05.19 - 04.06.19", "5", "5", "62 р.")
                    ).observable()

                    label("Затраты на хранение") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    tableview(tempMovementList) {
                        readonlyColumn("Номер склада", TempMovement::warehouseId)
                        readonlyColumn("Период", TempMovement::dateInterval)
                        readonlyColumn("Количество дней", TempMovement::daysCount)
                        readonlyColumn("Затраты", TempMovement::cost)
                    }

                    // TODO: This is old version
//                    tableview(cargoProperty.value.movementsList.observable()) {
//                        readonlyColumn("Id", Cargo.Movement::movementId)
//                        readonlyColumn("Старый статус", Cargo.Movement::prevStatus)
//                        readonlyColumn("Новый статус", Cargo.Movement::newStatus)
//                        readonlyColumn("Дата смены", Cargo.Movement::date)
//                        cargoProperty.onChange { items = cargoProperty.value.movementsList.observable() }
//                    }


                    data class TempOrderInfo(val orderId: String, val date:String, val type:String, val cost:String)
                    val orderInfoList = listOf(
                        TempOrderInfo("31", "29.05.19", "Внутренняя", "115 р."),
                        TempOrderInfo("39", "04.06.19", "Возврат", "560 р.")
                    ).observable()
                    label("Затраты на транспортировку") {style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) }}
                    tableview(orderInfoList) {
                        readonlyColumn("№ Заказа", TempOrderInfo::orderId)
                        readonlyColumn("Дата", TempOrderInfo::date)
                        readonlyColumn("Тип перевозки", TempOrderInfo::type)
                        readonlyColumn("Стоимость", TempOrderInfo::cost)
                    }

                    // TODO: This is old version
//                    label("Затраты на транспортировку") {style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) }}
//                    tableview(OrderListController.allOrders.filter { cargoProperty.value.orderIdList.contains(it.orderId) }.observable()) {
//                        readonlyColumn("№ Заказа", OrderInfo::orderId)
//                        readonlyColumn("№ Заказа", OrderInfo::orderId)
////                    readonlyColumn("Старый статус", Cargo.Movement::prevStatus)
////                    readonlyColumn("Новый статус", Cargo.Movement::newStatus)
////                    readonlyColumn("Дата смены", Cargo.Movement::date)
////                    cargoProperty.onChange { items = cargoProperty.value.movementsList.observable() }
//                        orferInfoListProperty.onChangeTimes(1000) {
//                            items = OrderListController.allOrders.filter { cargoProperty.value.orderIdList.contains(it.orderId) }.observable()
//                        }
//
//                    }
                }
            }
        }
    }

    /**
     * Динамика роста стоимости
     */

    class ProductItem(val name: String)
    class TabTwo : View("Динамика изменения стоимости") {

//        var selectedProducts: ObservableList<ProductListItem> = FXCollections.observableArrayList<ProductListItem>()
        var selectedProducts = listOf(
            ProductItem("Product #1"),
            ProductItem("Product #2"),
            ProductItem("Product #5")
        ).observable()

        /*For combobox*/
        var productList = ProductListController.productList
        var productListItem = SimpleObjectProperty<ProductListEntity.Product>(productList.firstOrNull())



        override val root = borderpane {
            center {

                vbox {
                    style {
                        backgroundColor += Color.WHITE
                    }
                    linechart("Изменение стоимости товаров с 20.05 по 24.05", CategoryAxis(), NumberAxis(0.0, 12000.0, 500.0)) {
                        series("Product #1") {
                            data("20.05", 2200)
                            data("21.05", 2792)
                            data("22.05", 2300)
                            data("23.05", 2500)
                            data("24.05", 1120)
                        }
                        series("Product #2") {
                            data("20.05", 5790)
                            data("21.05", 4020)
                            data("22.05", 4570)
                            data("23.05", 6030)
                            data("24.05", 7740)
                        }
                        series("Product #4") {
                            data("20.05", 10790)
                            data("21.05", 9020)
                            data("22.05", 9570)
                            data("23.05", 8030)
                            data("24.05", 10740)
                        }
                    }
                    piechart("Распределение по складам на конец периода") {
                        data("Склад #1",
                            (1120.0 + 7740.0 + 10740.0) / 0.2
                        )
                        data("Склад #2",
                            (1120.0 + 7740.0 + 10740.0) / 0.3

                        )
                        data("Склад #5",
                            (1120.0 + 7740.0 + 10740.0) / 0.5
                        )
                    }

                }

            }

            right {
                vbox {
                    style {
                        backgroundColor += Color.WHITE
                    }

                    label("Список складов") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    listview<String> {
                        items.add("Склад #1")
                        items.add("Склад #2")
                        items.add("Склад #5")
                    }

                    label("Список артикулов") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    listview<String> {
                        items.add("Product #1")
                        items.add("Product #1")
                        items.add("Product #2")
                        items.add("Product #5")
                    }
                    form {
                        fieldset {
                            field("Список артикулов") {
                                val deliveryTypes = listOf("Product #5", "Product #1")
                                val deliveryTypeItem = SimpleStringProperty(deliveryTypes.firstOrNull())
                                combobox(deliveryTypeItem, deliveryTypes)

                                //TODO: uncomment it
//                                combobox(productListItem, productList) {
//                                    cellFormat { text = it.name }
//                                }
                                button("Добавить").action {
                                    addOrUpdate(
                                        ProductItem("Product #1")
//                                    ProductListItem(
//                                        productListItem.value.productId!!,
//                                        productListItem.value.name!!,
//                                        productListItem.value.cost!!,
//                                        productListItem.value.height!!,
//                                        productListItem.value.width!!,
//                                        productListItem.value.length!!,
//                                        productListItem.value.weight!!,
//                                        1
//                                    )
                                    )
                                }
                            }
                            field("Список складов") {
                                val warehouses = listOf("Склад #5", "Склад #2", "Склад #3")
                                val warehouseItem = SimpleStringProperty(warehouses.firstOrNull())
                                combobox(warehouseItem, warehouses)

                                //TODO: implement warehouse list
//                                combobox(productListItem, productList) {
//                                    cellFormat { text = "Product: #5" }
//                                    cellFormat { text = it.name }
//                                }
                                button("Добавить").action {
                                    addOrUpdate(
                                        ProductItem("Product #1")
//                                    ProductListItem(
//                                        productListItem.value.productId!!,
//                                        productListItem.value.name!!,
//                                        productListItem.value.cost!!,
//                                        productListItem.value.height!!,
//                                        productListItem.value.width!!,
//                                        productListItem.value.length!!,
//                                        productListItem.value.weight!!,
//                                        1
//                                    )
                                    )
                                }
                            }

                            field("Начало периода") {
                                datepicker {
                                    value = LocalDate.now()
                                }

                            }
                            field("Конец периода") {
                                datepicker {
                                    value = LocalDate.now()
                                }
                            }
                            hbox {
                                spacingProperty().set(5.0)
                                button("Составить отчёт")
                                button("Очистить списки")
                            }
                        }
                    }

                }
//                form {
//                    fieldset {
//
//                        field("Список товаров") {
//                        }
//
////                        scrollpane {
////                            vbox {
////                                children.bind(selectedProducts) { product ->
////                                    hbox {
////                                        label(product.name)
////                                    }
////                                }
////                            }
////                        }
//
//
//
//                    }
//
//                }
            }
        }
        override val closeable = SimpleBooleanProperty(false)

        private fun addOrUpdate(product: ProductItem) {
            val target = selectedProducts.find { it.name == product.name }
            if (target == null) {
                selectedProducts.add(product)
            }
        }

    }


    class TabThree : View("Доходы") {
        private val productList = listOf(
            "Product #1",
            "Product #2",
            "Product #3",
            "Product #4",
            "Product #5"
        ).observable()

        override val root = borderpane {
            center {
                barchart("Доходы за период с 21.05 по 27.05", CategoryAxis(), NumberAxis()) {
                    series("Расходы") {
                        data("Proudct #1", -(890.0 + 420.0 + 4056.0)/0.3){ style { barFill = Color.ORANGERED } }
                        data("Proudct #2", -(890.0 + 420.0 + 4056.0)/0.2){ style { barFill = Color.ORANGERED } }
                        data("Proudct #3", -(890.0 + 420.0 + 4056.0)/0.1){ style { barFill = Color.ORANGERED } }
                        data("Proudct #4", -(890.0 + 420.0 + 4056.0)/0.4){ style { barFill = Color.ORANGERED } }
                    }
                    series("Доходы") {
                        data("Proudct #1", 9345 / 0.5)
                        data("Proudct #2", 9345 / 0.2)
                        data("Proudct #3", 9345 / 0.1)
                        data("Proudct #4", 9345 / 0.3)
                    }
                }
            }
            right {
                vbox {
                    style {
                        backgroundColor += Color.WHITE
                    }
                    label("Отчёт о доходах") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    textarea {
                        minHeightProperty().set(400.0)
                        text = """
                            Заказов доставлено: 11
                            На сумму: 9345 р.

                            Заказов отклонено: 2
                            Расходы: 890 р.

                            Внутренних перевозок: 4
                            Расходы: 420р.

                            Затраты на хранение: 4056 р.

                            Итого: + ${9345 - 890 - 420 - 4056}
                        """.trimIndent()
                    }

                    label("Список артикулов") { style { fontSize = Dimension(20.0, Dimension.LinearUnits.px) } }
                    listview<String> {
                        items.add("Product #1")
                        items.add("Product #2")
                        items.add("Product #3")
                        items.add("Product #4")
                    }

                    form {
                        fieldset {
                            field("Список артикулов") {
                                val deliveryTypes = listOf("Product #4", "Product #1")
                                val deliveryTypeItem = SimpleStringProperty(deliveryTypes.firstOrNull())
                                combobox(deliveryTypeItem, deliveryTypes)

                                //TODO: uncomment it
//                                combobox(productListItem, productList) {
//                                    cellFormat { text = it.name }
//                                }
                                button("Добавить").action {

                                }
                            }

                            field("Начало периода") {
                                datepicker {
                                    value = LocalDate.now()
                                }

                            }
                            field("Конец периода") {
                                datepicker {
                                    value = LocalDate.now()
                                }
                            }
                            hbox {
                                spacingProperty().set(5.0)
                                button("Составить отчёт")
                                button("Очистить")
                            }
                        }
                    }

                }
            }
        }
    }

}

class BarChartStyles : Stylesheet() {

    companion object {
        val backColor = c("#4682B4")
        val chartBar by cssclass()
    }

    init {
    }
}