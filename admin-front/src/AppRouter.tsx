import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import UsersMenuPage from "./components/users/UsersMenuPage/UsersMenuPage";
import ProductsMenuPage from "./components/products/ProductsMenuPage/ProductsMenuPage";
import LoginPage from "./components/general/loginPage/LoginPage";
import MenuPage from "./components/general/menuPage/MenuPage";
import AllUsersPage from "./components/users/AllUsersPage/AllUsersPage";
import ChangeUserInfoPage from "./components/users/ChangeUserInfoPage/ChangeUserInfoPage";
import FindUserPage from "./components/users/FindUserPage/FindUserPage";
import UserInfoPage from "./components/users/UserInfoPage/UserInfoPage";
import UserOrdersPage from "./components/users/UserOrdersPage/UserOrdersPage";
import AllProductsPage from "./components/products/AllProductsPage/AllProductsPage";
import ProductInfoPage from "./components/products/ProductInfoPage/ProductInfoPage";
import ChangeProductInfoPage from "./components/products/ChangeProductInfoPage/ChangeProductInfoPage";
import AddProductPage from "./components/products/AddProductPage/AddProductPage";
import FindProductByIdPage from "./components/products/FindProductByIdPage/FindProductByIdPage";
import FindProductByNamePage from "./components/products/FindProductByNamePage/FindProductByNamePage";
import InventoryMenuPage from "./components/inventory/InventoryMenuPage/InventoryMenuPage";
import AllInventoryPage from "./components/inventory/AllInventoryPage/AllInventoryPage";
import InventoryInfoPage from "./components/inventory/InventoryInfoPage/InventoryInfoPage";
import ChangeInventoryInfoPage from "./components/inventory/ChangeInventoryInfoPage/ChangeInventoryInfoPage";
import ChangeInventoryAmountPage from "./components/inventory/ChangeInventoryAmountPage/ChangeInventoryAmountPage";
import AddInventoryPage from "./components/inventory/AddInventoryPage/AddInventoryPage";
import FindInventoryByIdPage from "./components/inventory/FindInventoryByIdPage/FindInventoryByIdPage";
import FindInventoryByNamePage from "./components/inventory/FindInventoryByNamePage/FindInventoryByNamePage";
import OrdersMenuPage from "./components/orders/OrdersMenuPage/OrdersMenuPage";
import AllOrdersPage from "./components/orders/AllOrdersPage/AllOrdersPage";
import OrdersInfoPage from "./components/orders/OrdersInfoPage/OrdersInfoPage";
import FindOrderByIdPage from "./components/orders/FindOrderByIdPage/FindOrderByIdPage";
import FindUserOrdersPage from "./components/orders/FindUserOrdersPage/FindUserOrdersPage";
import OrdersHistoryPage from "./components/orders/OrdersHistoryPage/OrdersHistoryPage";

const AppRouter: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/menu" element={<MenuPage />} />
        <Route path="/" element={<LoginPage />} />

        <Route path="/users/menu" element={<UsersMenuPage />} />
        <Route path="/users/show" element={<AllUsersPage />} />
        <Route path="/users/find" element={<FindUserPage />} />
        <Route path="/users/:username/show" element={<UserInfoPage />} />
        <Route
          path="/users/:username/orders/show"
          element={<UserOrdersPage />}
        />
        <Route
          path="/users/:username/changeInfo"
          element={<ChangeUserInfoPage />}
        />

        <Route path="/products/menu" element={<ProductsMenuPage />} />
        <Route path="/products/add" element={<AddProductPage />} />
        <Route path="/products/show" element={<AllProductsPage />} />
        <Route path="/products/find/id" element={<FindProductByIdPage />} />
        <Route path="/products/find/name" element={<FindProductByNamePage />} />
        <Route path="/products/:name/show" element={<ProductInfoPage />} />
        <Route
          path="/products/:id/changeInfo"
          element={<ChangeProductInfoPage />}
        />

        <Route path="/inventory/menu" element={<InventoryMenuPage />} />
        <Route path="/inventory/show" element={<AllInventoryPage />} />
        <Route path="/inventory/add" element={<AddInventoryPage />} />
        <Route path="/inventory/find/id" element={<FindInventoryByIdPage />} />
        <Route
          path="/inventory/find/name"
          element={<FindInventoryByNamePage />}
        />
        <Route path="/inventory/:name/show" element={<InventoryInfoPage />} />
        <Route
          path="/inventory/:id/changeInfo"
          element={<ChangeInventoryInfoPage />}
        />
        <Route
          path="/inventory/:id/changeAmount"
          element={<ChangeInventoryAmountPage />}
        />

        <Route path="/orders/menu" element={<OrdersMenuPage />} />
        <Route path="/orders/show" element={<AllOrdersPage />} />
        <Route path="/orders/:id/show" element={<OrdersInfoPage />} />
        <Route path="/orders/find/id" element={<FindOrderByIdPage />} />
        <Route path="/orders/find/username" element={<FindUserOrdersPage />} />
        <Route path="/orders/history" element={<OrdersHistoryPage />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
