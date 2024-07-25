import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import ProductsMenuContent from "./ProductsMenuContent";
import MainHeader from "../../general/loginPage/MainHeader";

const ProductsMenuPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return (
      <>
        <ErrorUnauthorizedPage />
      </>
    );
  }
  return (
    <>
      <MainHeader />
      <ProductsMenuContent />
    </>
  );
};

export default ProductsMenuPage;
