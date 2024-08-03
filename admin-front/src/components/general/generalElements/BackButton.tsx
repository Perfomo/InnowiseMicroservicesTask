import { Button } from "antd";
import { useLocation } from "react-router-dom";

const BackButton = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  const pathSegments = currentPath.split("/").filter((segment) => segment);
  const trimmedPathSegments = [
    ...pathSegments.slice(0, -2),
    ...pathSegments.slice(-1), 
  ];
  const trimmedPath = "/" + trimmedPathSegments.join("/");
  return (
    <Button
      href={trimmedPath}
      type="primary"
      ghost
      style={{
        width: "50%",
        height: "5vh",
        fontWeight: 700,
      }}
    >
      Back
    </Button>
  );
};

export default BackButton;
