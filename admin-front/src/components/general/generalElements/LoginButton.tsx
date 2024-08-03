import { Button } from "antd";

const LoginButton = () => {
  return (
    <Button
      href="/"
      type="primary"
      ghost
      style={{ width: "100%", height: "6vh", fontWeight: 700 }}
    >
      Login
    </Button>
  );
};

export default LoginButton;
