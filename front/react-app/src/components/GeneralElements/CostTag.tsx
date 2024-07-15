import { Button } from "antd";

interface CostTagProps {
    cost: string | null;
}

const CostTag = ({cost}: CostTagProps) => {
  return (
    <Button
      href="/cart"
      type="primary"
      ghost
      style={{ height: "4vh", fontWeight: 700 }}
    >
      {cost}$
    </Button>
  );
};

export default CostTag;
