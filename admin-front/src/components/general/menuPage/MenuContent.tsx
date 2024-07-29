import { Flex, Layout } from "antd";
import CustomPrimaryButton from "../generalElements/CustomPrimaryButton";

const MenuContent: React.FC = () => {
  return (
    <>
      <Layout style={{ backgroundColor: "white" }}>
        <Flex
          justify="center"
          align="normal"
          wrap={true}
          style={{
            width: "94%",
            marginLeft: "3%",
            marginRight: "3%",
          }}
        >
          <CustomPrimaryButton path="/users/menu" text="Users" />
          <CustomPrimaryButton path="/products/menu" text="Products" />
          <CustomPrimaryButton path="/inventory/menu" text="Inventory" />
        </Flex>
      </Layout>
    </>
  );
};

export default MenuContent;
