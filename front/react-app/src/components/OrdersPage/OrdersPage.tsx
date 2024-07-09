import React from "react";
import MainHeader from "../mainPage/MainHeader";
import ErrorUnauthorizedPage from "../ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import OrdersContent from "./OrdersContent";

const OrdersPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />
  }
  return (
    <>
      <MainHeader />
      <OrdersContent />
    </>  
  );
};

export default OrdersPage;
