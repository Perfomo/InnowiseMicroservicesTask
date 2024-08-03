import MainHeader from "../loginPage/MainHeader";
import ErrorUnauthorizedContent from "./ErrorUnauthorizedContent";

const ErrorUnauthorizedPage: React.FC = () => {
  return (
    <>
      <MainHeader />
      <ErrorUnauthorizedContent />
    </>
  );
};

export default ErrorUnauthorizedPage;
