import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import AllOrdersContent from "./AllOrdersContent";


const AllOrdersPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <AllOrdersContent />
    </>
  );
};

export default AllOrdersPage;
