import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import FindInventory from "../generalComponents/FindInventory";

const FindInventoryByIdPage: React.FC = () => {
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
      <FindInventory searchBy="id" />
    </>
  );
};

export default FindInventoryByIdPage;
