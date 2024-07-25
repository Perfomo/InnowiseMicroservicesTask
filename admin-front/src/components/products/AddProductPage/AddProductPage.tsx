import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import AddProductForm from "./AddProductForm";


const AddProductPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <AddProductForm />
    </>
  );
};

export default AddProductPage;
