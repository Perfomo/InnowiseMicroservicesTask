import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
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
