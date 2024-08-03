import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import ChangeProductInfoForm from "./ChangeProductInfoForm";

const ChangeProductInfoPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <ChangeProductInfoForm />
    </>
  );
};

export default ChangeProductInfoPage;
