import ErrorUnauthorizedPage from "../ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../mainPage/MainHeader";
import ChangeUserInfoForm from "./ChangeUserInfoForm";

const ChangeUserInfoPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <ChangeUserInfoForm />
    </>
  );
};

export default ChangeUserInfoPage;
