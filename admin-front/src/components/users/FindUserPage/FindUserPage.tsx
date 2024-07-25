import React from "react";

import FindUserContent from "./FindUserContent";
import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";

const FindUserPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <FindUserContent />
    </>
  );
};

export default FindUserPage;
