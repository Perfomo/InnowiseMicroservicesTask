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
        <Route path="/products/find/id/:id" element={<FindProductByIdPage />} />
        <Route path="/products/:name/show" element={<ProductInfoPage />} />
        <Route
          path="/products/:id/changeInfo"
          element={<ChangeProductInfoPage />}
        />
      </Routes>
    </Router>
  );
};

export default AppRouter;
