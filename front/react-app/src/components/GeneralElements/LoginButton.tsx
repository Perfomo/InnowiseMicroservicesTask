import { Button } from "antd";

const LoginButton = () => {
  return (
    <Button
      href="/login"
      type="primary"
      ghost
      style={{ width: "40%", height: "6vh", fontWeight: 700 }}
    >
      Login
    </Button>
  );
};

export default LoginButton;
