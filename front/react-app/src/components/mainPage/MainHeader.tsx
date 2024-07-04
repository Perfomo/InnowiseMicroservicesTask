import { Flex, Row, Col, Layout } from "antd";
import LoginButton from "../GeneralElements/LoginButton";
import RegisterButton from "../GeneralElements/RegisterButton";
import OrderCartButton from "../GeneralElements/OrderCartButton";
import LogoButton from "../GeneralElements/LogoButton";
import ProfileButton from "../GeneralElements/ProfileButton";

const mainHeaderRender = () => {
  if (localStorage.getItem("token")) {
    return (
      <>
        <ProfileButton />
      </>
    )
  }
  return (
    <>
      <LoginButton />
      <RegisterButton />
    </>
  )
}

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
            style={{ height: "100%", marginRight: "2%" }}
            gap="middle"
          >
            {mainHeaderRender()}
            <OrderCartButton />
          </Flex>
        </Col>
      </Row>
    </Layout>
  );
};

export default MainHeader;
