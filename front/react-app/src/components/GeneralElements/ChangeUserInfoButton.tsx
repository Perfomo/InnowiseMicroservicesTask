import { Button } from "antd";

const ChangeUserInfoButton = () => {
  return (
    <Button
      href="/changeUserInfo"
      type="primary"
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "50%",
        height: "6vh",
        marginTop: "2%",
        fontWeight: 700,
      }}
    >
      Change info
    </Button>
  );
};

export default ChangeUserInfoButton;