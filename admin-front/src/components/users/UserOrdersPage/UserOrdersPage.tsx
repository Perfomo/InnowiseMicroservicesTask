import React from "react";
import UserOrdersContent from "./UserOrdersContent";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";

const OrdersPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />
  }
  return (
    <>
      <MainHeader />
      <UserOrdersContent />
    </>  
  );
};

export default OrdersPage;
