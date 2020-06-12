package ru.delivery.system.views.modal

import tornadofx.*

class DocumentPreview : View("Предпросмотр") {
    override val root = borderpane {
        center {
            imageview("tornado.jpg") {
                scaleX = .50
                scaleY = .50
            }
        }
        bottom {
            button("Создать")
            button("Отмена")
        }

    }
}
