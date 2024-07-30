import { useState } from "react";
import { UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, InputNumber, Layout, Alert } from "antd";
import { useNavigate } from "react-router-dom";
import TokenManager from "../../general/generalElements/TokenManager";

const FindOrderByIdFormContent = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [error, setError] = useState<string | null>(null);

  const onFinish = async (values: { id?: number }) => {
    let value = null;
    let path = null;
    value = values.id;
    path = "/api/orders/api/orders/";
    try {
      const response = await fetch(path + value, {
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
      console.log(values);
      console.log(value);
      navigate(`/orders/${data.id}/show`, {
        state: { order: data },
      });
    } catch (error) {
      console.error("Error during getting order: ", error);
    }
  };

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
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
            name="id"
            rules={[
              { required: true, message: "Please input id!" },
              { type: "number", message: "Input number!" },
            ]}
          >
            <InputNumber
              style={{ width: "100%" }}
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Id"
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

export default FindOrderByIdFormContent;
