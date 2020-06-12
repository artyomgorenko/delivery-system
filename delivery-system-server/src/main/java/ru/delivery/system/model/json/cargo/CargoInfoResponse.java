package ru.delivery.system.model.json.cargo;

import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.common.enums.ProductStatus;

import javax.json.bind.annotation.JsonbProperty;
import java.util.Date;
import java.util.List;

public class CargoInfoResponse {

    @JsonbProperty("cargoList")
    @Getter @Setter
    private List<Cargo> cargoList;

    public static class Cargo {

        /**
         * Id самого товара
         */
        @JsonbProperty("cargoId")
        @Getter @Setter
        private Integer cargoId;

        /**
         * Id артикула продукции
         */
        @JsonbProperty("productId")
        @Getter @Setter
        private Integer productId;

        /**
         * Номер склада, на котором хранится товар
         */
        @JsonbProperty("warehouseId")
        @Getter @Setter
        private Integer warehouseId;

        /**
         * Список номеров заказов, в которых учавствовал товар
         */
        @JsonbProperty("orderIdList")
        @Getter @Setter
        private List<Integer> orderIdList;

        /**
         * Стаутс товара
         *
         * @see ProductStatus
         */
        @JsonbProperty("status")
        @Getter @Setter
        private String status;

        /**
         * История изменений статустов конкретного товара
         */
        @JsonbProperty("movementsList")
        @Getter @Setter
        private List<Movement> movementsList;

        public static class Movement {
            @JsonbProperty("movementId")
            @Getter @Setter
            private Integer movementId;

            @JsonbProperty("newStatus")
            @Getter @Setter
            private String newStatus;

            @JsonbProperty("prevStatus")
            @Getter @Setter
            private String prevStatus;

            @JsonbProperty("date")
            @Getter @Setter
            private Date date;
        }
    }
}
