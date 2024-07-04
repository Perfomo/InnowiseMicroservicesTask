import React from "react";
import MainHeader from "../mainPage/MainHeader";
import ErrorUnauthorizedPage from "../ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import ProfileContent from "./ProfileContent";

const ProfilePage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />
  }
  return (
    <>
      <MainHeader />
      <ProfileContent />
    </>  
  );
};

export default ProfilePage;
