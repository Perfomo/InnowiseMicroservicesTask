import ErrorUnauthorizedPage from "../../general/ErrorUnauthorizedPage/ErrorUnauthorizedPage";
import MainHeader from "../../general/loginPage/MainHeader";
import ChangeInventoryAmountForm from "./ChangeInventoryAmountForm";

const ChangeInventoryAmountPage: React.FC = () => {
  if (!localStorage.getItem("token")) {
    return <ErrorUnauthorizedPage />;
  }
  return (
    <>
      <MainHeader />
      <ChangeInventoryAmountForm />
    </>
  );
};

export default ChangeInventoryAmountPage;
