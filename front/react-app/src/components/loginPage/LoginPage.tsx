import MainHeader from "../mainPage/MainHeader";
import LoginForm from "./LoginForm";


const LoginPage: React.FC = () => {
  return (
    <>  
        {console.log(localStorage.getItem("token"))}
        <MainHeader />
        <LoginForm />
    </>  
  );
};

export default LoginPage;
