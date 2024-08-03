import React from "react";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import UsersMenuContent from "./UsersMenuContent";
import MainHeader from "../../general/loginPage/MainHeader";

const UsersMenuPage: React.FC = () => {
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
      <UsersMenuContent />
    </>
  );
};

export default UsersMenuPage;
