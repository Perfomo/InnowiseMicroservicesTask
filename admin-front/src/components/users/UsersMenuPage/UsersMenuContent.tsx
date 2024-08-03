import { Flex, Layout } from "antd";
import CustomPrimaryButton from "../../general/generalElements/CustomPrimaryButton";

const UsersMenuContent: React.FC = () => {
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
          <CustomPrimaryButton path="/users/show" text="Show users" />
          <CustomPrimaryButton path="/users/find" text="Find user" />
        </Flex>
      </Layout>
    </>
  );
};

export default UsersMenuContent;
