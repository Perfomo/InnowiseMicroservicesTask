import { Button } from "antd";

const RegisterButton = () => {
  return (
    <Button
      href="/register"
      type="primary"
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "40%",
        height: "6vh",
        fontWeight: 700,
      }}
    >
      Register
    </Button>
  );
};

export default RegisterButton;
