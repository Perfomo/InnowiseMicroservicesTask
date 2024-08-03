import { Flex, Layout } from "antd";
import CustomPrimaryButton from "../../general/generalElements/CustomPrimaryButton";

const OrdersMenuContent: React.FC = () => {
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
          <CustomPrimaryButton path="/orders/show" text="Show orders" />
          <CustomPrimaryButton
            path="/orders/find/id"
            text="Find order by id"
          />
          <CustomPrimaryButton
            path="/orders/find/username"
            text="Find user's orders "
          />
          <CustomPrimaryButton path="/orders/history" text="Show history" />
        </Flex>
      </Layout>
    </>
  );
};

export default OrdersMenuContent;
