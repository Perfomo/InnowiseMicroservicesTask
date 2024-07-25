import { Flex, Row, Col, Layout } from "antd";
import LoginButton from "../generalElements/LoginButton";
import LogoButton from "../generalElements/LogoButton";
import LogoutButton from "../generalElements/LogoutButton";


const mainHeaderRender = () => {
  if (localStorage.getItem("token")) {
    return (
      <>
        {localStorage.getItem("username")}
        <LogoutButton />
      </>
    );
  }
  return (
    <>
      <LoginButton />
    </>
  );
};

export const MainHeader = () => {
  return (
    <Layout>
      <Row style={{ height: "10vh" }} justify={"space-between"}>
        <Col>
          <Flex
            justify="flex-start"
            align="center"
            color="blue"
            style={{ height: "100%", padding: "0%" }}
          >
            <LogoButton />
          </Flex>
        </Col>
        <Col style={{ justifyContent: "right" }}>
          <Flex
            justify="space-around"
            align="center"
            color="blue"
            style={{ height: "100%", marginRight: "25%" }}
            gap="middle"
          >
            {mainHeaderRender()}
          </Flex>
        </Col>
      </Row>
    </Layout>
  );
};

export default MainHeader;
