import { Button } from "antd";
import axios from "axios";

const onClick = (values: any) => {
  axios
    .delete(
      "http://172.17.0.1:8081/users/api/users/" +
        localStorage.getItem("username"),
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    )
    .catch((error) => {
        console.log(error)
    });
    localStorage.clear()
};

const DeleteUserButton = () => {
  return (
    <Button
      onClick={onClick}
      href="/"
      type="primary"
      ghost
      style={{
        width: "50%",
        height: "6vh",
        fontWeight: 700,
        color: "red",
        borderColor: "red",
      }}
    >
      Delete User
    </Button>
  );
};

export default DeleteUserButton;
