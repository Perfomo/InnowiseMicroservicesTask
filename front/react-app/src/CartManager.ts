import { json } from "react-router-dom";

interface Product {
    name: string,
    cost: number,
    quantity: number
}

export default class CartManager {
    
    private static getTotalPriceFromLocalStorage() {
        let price = localStorage.getItem("cartCost");
        if (price) {
            return JSON.parse(price);
        }
        return 0;
    }

    public static getCartFromLocalStorage() {
        const cart = localStorage.getItem("cart")
        if (cart) {
            return JSON.parse(cart);
        }
        localStorage.setItem("cartCost", JSON.stringify(0))
        return [];
    }

    public static getProductQuantityFromCart(name: string) {
        const cart = this.getCartFromLocalStorage() as Product[];
        const existingProduct = cart.find(product => product.name === name);
        if (existingProduct) {
            return existingProduct.quantity
        }
        return 0
    }

    public static addProduct(name: string, cost: number) {
        let cart = this.getCartFromLocalStorage() as Product[];
        let totalPrice = this.getTotalPriceFromLocalStorage();
        const existingProduct = cart.find(product => product.name === name);
        if (existingProduct) {
            existingProduct.quantity += 1;
        }
        else {
            cart.push({
                name, cost,
                quantity: 1
            });
        }
        localStorage.setItem("cartCost", JSON.stringify(Math.round((totalPrice + cost) * 100000) / 100000))
        localStorage.setItem("cart", JSON.stringify(cart));
    }

    public static removeProduct(name: string) {
        let cart = this.getCartFromLocalStorage() as Product[];
        let totalPrice = this.getTotalPriceFromLocalStorage();
        const existingProduct = cart.find(product => product.name === name);
        if (existingProduct) {

            if (existingProduct.quantity > 0) {
                localStorage.setItem("cartCost", JSON.stringify(Math.round((totalPrice - existingProduct.cost) * 100000) / 100000))
                existingProduct.quantity -= 1;
            }
            if (existingProduct.quantity <= 0) {
                cart = cart.filter(product => product.name !== name);
            }
            localStorage.setItem("cart", JSON.stringify(cart));
        }
    }

    public static parseCartToOrderRequest() {
        const cart = this.getCartFromLocalStorage();
        const createRequestBody = (products: Product[]): { products: { [name: string]: number } } => {
            const productsObject: { [name: string]: number } = {};
        
            products.forEach(product => {
                productsObject[product.name] = product.quantity;
            });
        
            return { products: productsObject };
        };
        return JSON.stringify(createRequestBody(cart));
    }
}