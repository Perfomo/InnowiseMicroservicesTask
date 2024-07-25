import { Button } from "antd";
import axios from "axios";

interface DeleteButtonProps {
  searchEl: string;
  type: string
}

const onClick = (searchEl: string, type: string) => {
  axios
    .delete(
      "/api/" + type + "/api/" + type + "/" + searchEl,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    )
    .catch((error) => {
        console.log(error)
    });
};

const DeleteButton = ({searchEl, type}: DeleteButtonProps) => {
  return (
    <Button
      onClick={() => onClick(searchEl, type)}
      href={"/" + type + "/show"}
      type="primary"
      ghost
      style={{
        width: "50%",
        height: "5vh",
        fontWeight: 700,
        color: "red",
        borderColor: "red",
      }}
    >
      Delete {type[0] === "s" ? type.slice(0,-1) : type}
    </Button>
  );
};

export default DeleteButton;
