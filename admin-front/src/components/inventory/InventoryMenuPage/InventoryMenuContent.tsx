import { Flex, Layout } from "antd";
import CustomPrimaryButton from "../../general/generalElements/CustomPrimaryButton";

const InventoryMenuContent: React.FC = () => {
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
          <CustomPrimaryButton path="/inventory/show" text="Show inventory" />
          <CustomPrimaryButton
            path="/inventory/find/id"
            text="Find inventory by id"
          />
          <CustomPrimaryButton
            path="/inventory/find/name"
            text="Find inventory by name"
          />
          <CustomPrimaryButton path="/inventory/add" text="Add inventory" />
        </Flex>
      </Layout>
    </>
  );
};

export default InventoryMenuContent;
