import { Button } from "antd";

interface customPrimaryButtonProps {
  path: string,
  text: string
}

const customPrimaryButton = ({path, text}: customPrimaryButtonProps) => {
  return (
    <Button
      href={path}
      type="primary"
      ghost
      style={{ width: "20%", height: "10vh", fontWeight: 700, margin: "5%" }}
    >
      {text}
    </Button>
  );
};

export default customPrimaryButton;
