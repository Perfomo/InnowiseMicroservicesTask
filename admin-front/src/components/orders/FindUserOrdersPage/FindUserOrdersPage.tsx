import React, { useState } from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import FindUserOrdersContent from "./FindUserOrdersContent";
import FindUserOrdersForm from "./FindUserOrdersForm";

const FindUserOrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<any[]>([]);
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
      <FindUserOrdersForm setUserOrders={setOrders} />
      <FindUserOrdersContent orders={orders} />
    </>
  );
};

export default FindUserOrdersPage;
