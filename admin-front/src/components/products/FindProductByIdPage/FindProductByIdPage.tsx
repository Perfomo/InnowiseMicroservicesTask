import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import ProductInfoContent from "./ProductInfoContent";

const FindProductByIdPage: React.FC = () => {
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
      <FindProductByIdForm />
    </>
  );
};

export default FindProductByIdPage;
