import { Button } from "antd";
import axios from "axios";

const onClick = () => {
  console.log("in")
  axios
  .delete("/users/api/logout", {
    headers: {
      "Authorization": "Bearer " + localStorage.getItem("token"),
    },
  })
  .then ((response) => {
    console.log("logout completed")
  })
  .catch((error) => {
    console.log(error);
  });
  console.log("here")
  localStorage.clear();
};

const LogoutButton = () => {
  return (
    <Button
      href="/"
      type="primary"
      onClick={onClick}
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
