package ru.delivery.system.views.child_screens

import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.json.ProductListEntity
import ru.delivery.system.models.viewmodels.Product
import ru.delivery.system.models.viewmodels.ProductModel
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.common.ChildScreenHeader
import tornadofx.*

class ProductScreen : View("Заказы") {

    companion object {
        const val PRODUCT_ID_COLUMN = "Номер заказа"
        const val STATUS_COLUMN = "Статус заказа"
        const val DEPARTURE_POINT_COLUMN = "Пункт отправления"
        const val DESTINATION_POINT_COLUMN = "Пункт назначения"
        const val DELIVERY_TIME_COLUMN = "Время доставки"
    }

    private var products = mutableListOf<Product>(

    ).observable()
    private val httpHelper = HttpHelper
    private val model = ProductModel(Product())

    override fun onDock() {
        val response = HttpHelper.syncGet("product/productList")
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val productListEntity = JsonSerializer().toEntity<ProductListEntity>(body.string())
                val productList = productListEntity.productList!!
                products.clear()
                products.addAll(productList.map {  product ->
                    Product().apply {
                        product.productId?.let { productId = it}
                        product.name?.let { name = it}
                        product.cost?.let { cost = it}
                        product.category?.let { category = it}
                        product.weight?.let { weight = it}
                        product.height?.let { height = it}
                        product.width?.let { width = it}
                        product.length?.let { length = it}
                    }
                })
            }
        }
        super.onDock()
    }

    override val root = borderpane {
        top(ChildScreenHeader::class)
        center {
            tableview(products) {
                column(PRODUCT_ID_COLUMN, Product::productId)
                column(PRODUCT_ID_COLUMN, Product::name)
                column(PRODUCT_ID_COLUMN, Product::cost)
                column(PRODUCT_ID_COLUMN, Product::category)
                column(PRODUCT_ID_COLUMN, Product::weight)
                column(PRODUCT_ID_COLUMN, Product::height)
                column(PRODUCT_ID_COLUMN, Product::width)
                column(PRODUCT_ID_COLUMN, Product::length)

                // Update the person inside the view model on selection change
                model.rebindOnChange(this) { selectedOrder ->
                    item = selectedOrder ?: Product()
                }
            }
        }

        right {
            vbox {
                form {
                    fieldset("Редактирование товара") {
                        field(PRODUCT_ID_COLUMN) {
                            textfield(model.productId)
                        }
                        field(STATUS_COLUMN) {
                            textfield(model.name)
                        }
                        field(DEPARTURE_POINT_COLUMN) {
                            textfield(model.category)
                        }
                        field(DESTINATION_POINT_COLUMN) {
                            textfield(model.weight)
                        }
                        field(DELIVERY_TIME_COLUMN) {
                            textfield(model.height)
                        }
                        field(DELIVERY_TIME_COLUMN) {
                            textfield(model.width)
                        }
                        field(DELIVERY_TIME_COLUMN) {
                            textfield(model.length)
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
                }
            }
        }
        bottom {
            hbox {
                button("Создать товар").action { find<OrderCreateScreen>().openModal() }
            }
        }
    }

    private fun save() {
        // Flush changes from the text fields into the model
        model.commit()

        // The edited person is contained in the model
        val product = model.product

        // A real application would persist the person here
        println("Saving ${product.productId} / ${product.category}")
    }
}

