import React from "react";
import UserInfoContent from "./UserInfoContent";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";

const UserInfoPage: React.FC = () => {
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
      <UserInfoContent />
    </>
  );
};

export default UserInfoPage;
