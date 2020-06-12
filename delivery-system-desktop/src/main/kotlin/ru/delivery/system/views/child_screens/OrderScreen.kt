package ru.delivery.system.views.child_screens

import javafx.beans.property.SimpleStringProperty
import ru.delivery.system.common.ExcelUtils
import ru.delivery.system.controllers.OrderListController
import ru.delivery.system.models.viewmodels.Order
import ru.delivery.system.models.viewmodels.OrderModel
import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*

class OrderScreen : View("Заказы") {

    companion object {
        const val ORDER_ID_COLUMN = "Номер заказа"
        const val STATUS_COLUMN = "Статус заказа"
        const val DEPARTURE_POINT_COLUMN = "Пункт отправления"
        const val DESTINATION_POINT_COLUMN = "Пункт назначения"
        const val DELIVERY_TIME_COLUMN = "Время доставки"
    }

    private var orders = OrderListController.gerOrderList().observable()
    private var orderProductList = listOf(
        SimpleStringProperty("Product#1"),
        SimpleStringProperty("Product#2")
    ).observable()
    private val model = OrderModel(Order())



    override fun onDock() {
        orders.clear()
        orders.addAll(OrderListController.gerOrderList())
        super.onDock()
    }

    override val root = borderpane {
        top(ChildScreenHeader::class)
        center {
            tableview(orders) {
                column(ORDER_ID_COLUMN, Order::orderIdProperty)
                column(STATUS_COLUMN, Order::statusProperty)
                column(DEPARTURE_POINT_COLUMN, Order::departurePointProperty)
                column(DESTINATION_POINT_COLUMN, Order::destinationPointProperty)
                column(DELIVERY_TIME_COLUMN, Order::deliveryTimeProperty)

                // Update the person inside the view model on selection change
                model.rebindOnChange(this) { selectedOrder ->
                    item = selectedOrder ?: Order()
                }
            }
        }

        right {
            vbox {
                form {
                    fieldset("Редактирование заказа") {
                        field(ORDER_ID_COLUMN) {
                            textfield(model.orderId)
                        }
                        field(STATUS_COLUMN) {
                            textfield(model.status)
                        }
                        field(DEPARTURE_POINT_COLUMN) {
                            textfield(model.departurePoint)
                        }
                        field(DESTINATION_POINT_COLUMN) {
                            textfield(model.destinationPoint)
                        }
                        field(DELIVERY_TIME_COLUMN) {
                            textfield(model.deliveryTime)
                        }
                        button("Сохранить") {
                            enableWhen(model.dirty)
                            action {
                                save()
                            }
                        }
                        button("Отменить изменения").action {
                            model.rollback()
                        }
                    }

                    data class TempProduct(val name:String, val count:String)
                    val tempProductList = listOf(
                        TempProduct("Product #1", "2"),
                        TempProduct("Product #2", "3"),
                        TempProduct("Product #4", "1")
                    ).observable()
                    fieldset("Список товаров") {
                        tableview(tempProductList/*orderProductList*/) {
                            column("Название", SimpleStringProperty::getValue)
                            column("Количество", SimpleStringProperty::getValue)
                        }
                    }
                    hbox {
                        spacingProperty().set(5.0)
                        button("Создать ТТН").action { ExcelUtils().createTtnDoc() }
                        button("Создать Путевой лист")
                    }
                }
            }
        }
        bottom {
            hbox {
                button("Создать заказ").action { find<OrderCreateScreen>().openModal() }
            }
        }
    }

    private fun save() {
        // Flush changes from the text fields into the model
        model.commit()

        // The edited person is contained in the model
        val order = model.order

        // A real application would persist the person here
        println("Saving ${order.orderId} / ${order.status}")
    }
}
