import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import AllInventoryContent from "./AllInventoryContent";


const AllInventoryPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <AllInventoryContent />
    </>
  );
};

export default AllInventoryPage;
