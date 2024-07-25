import React from "react";
import MenuContent from "./MenuContent";
import ErrorUnauthorizedPage from "../ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../loginPage/MainHeader";

const MenuPage: React.FC = () => {
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
      <MenuContent />
    </>
  );
};

export default MenuPage;
