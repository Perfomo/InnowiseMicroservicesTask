import { Button } from "antd";

const LogoutButton = () => {
  return (
    <Button
      href="/"
      type="primary"
      onClick={() => localStorage.clear()}
      ghost
      style={{
        width: "50%",
        height: "6vh",
        fontWeight: 700,
        color: "red",
        borderColor: "red",
        marginTop: "2%"
      }}
    >
      Logout
    </Button>
  );
};

export default LogoutButton;
