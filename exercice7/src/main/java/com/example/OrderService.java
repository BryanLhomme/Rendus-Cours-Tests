package com.example;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderReceipt placeOrder(Order order, ClientType clientType) {
        if (!productRepository.exists(order.getProductReference())) {
            throw new IllegalArgumentException("Produit introuvable : " + order.getProductReference());
        }

        Product product = productRepository.findByReference(order.getProductReference());

        if (order.getQuantity() > product.getStock()) {
            throw new IllegalStateException("Stock insuffisant pour le produit " + order.getProductReference());
        }

        double total = product.getUnitPrice() * order.getQuantity();
        double totalAfterDiscount = total * (1 - clientType.getDiscountRate() / 100.0);

        return new OrderReceipt(
                order.getProductReference(),
                order.getQuantity(),
                totalAfterDiscount,
                "Commande confirmée"
        );
    }
}
