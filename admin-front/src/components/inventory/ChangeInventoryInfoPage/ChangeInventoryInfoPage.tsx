import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import ChangeInventoryInfoForm from "./ChangeInventoryInfoForm";

const ChangeInventoryInfoPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <ChangeInventoryInfoForm />
    </>
  );
};

export default ChangeInventoryInfoPage;
