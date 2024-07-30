import { useState } from "react";
import { UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, Layout, Alert, Input } from "antd";
import TokenManager from "../../general/generalElements/TokenManager";

interface FindUserOrdersFormProps {
  setUserOrders: React.Dispatch<React.SetStateAction<any[]>>;
}

const FindUserOrdersForm = ({ setUserOrders }: FindUserOrdersFormProps) => {
  const [form] = Form.useForm();
  const [error, setError] = useState<string | null>(null);

  const onFinish = async (values: { username: string }) => {
    console.log("start");
    const value = values.username;
    const path = "/api/orders/api/" + value + "/orders";
    console.log(path);
    try {
      const response = await fetch(path, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });

      if (!response.ok) {
        if (response.status === 401) {
          try {
            TokenManager.tokenRefresh();
            window.location.reload();
          } catch (error) {
            console.log("error: " + error);
          }
        }

        if (response.status === 404) {
          setError("404 Error: Order not found");
        }

        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log(data)
      console.log(values);
      console.log(value);
      setUserOrders(data);
      console.log("ok");
    } catch (error) {
      console.error("Error during getting orders: ", error);
    }
  };

  return (
    <Layout style={{backgroundColor: "white", marginTop:"5%"}}>
      <Flex
        justify="center"
        align="center"
        style={{width: "100%" }}
      >
        <Form
          form={form}
          name="normal_login"
          className="login-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
          {error && <Alert message={error} type="error" showIcon />}

          <Form.Item
            name="username"
            rules={[{ required: true, message: "Please input username!" }]}
          >
            <Input
              style={{ width: "100%" }}
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Username"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              style={{ width: "100%" }}
            >
              Find
            </Button>
          </Form.Item>
        </Form>
      </Flex>
    </Layout>
  );
};

export default FindUserOrdersForm;
