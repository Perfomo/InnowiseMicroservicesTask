import { Button } from "antd";

const ProfileButton = () => {
  return (
    <Button
      href="/profile"
      type="primary"
      ghost
      style={{ width: "50%", height: "6vh", fontWeight: 700 }}
    >
      Profile
    </Button>
  );
};

export default ProfileButton;