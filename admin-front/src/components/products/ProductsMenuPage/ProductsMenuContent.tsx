import { Flex, Layout } from "antd";
import CustomPrimaryButton from "../../general/generalElements/CustomPrimaryButton";

const ProductsMenuContent: React.FC = () => {
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
          <CustomPrimaryButton path="/products/show" text="Show products" />
          <CustomPrimaryButton
            path="/products/find/id"
            text="Find product by id"
          />
          <CustomPrimaryButton
            path="/products/find/name"
            text="Find product by name"
          />
          <CustomPrimaryButton path="/products/add" text="Add product" />
        </Flex>
      </Layout>
    </>
  );
};

export default ProductsMenuContent;
