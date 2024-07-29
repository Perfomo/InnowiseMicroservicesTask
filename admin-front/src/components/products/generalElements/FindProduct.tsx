import React, { useState } from "react";
import { UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, Input, InputNumber, Layout, Alert } from "antd";
import { useNavigate } from "react-router-dom";
import TokenManager from "../../general/generalElements/TokenManager";

interface FindProductProps {
  searchBy: string;
}

const FindProductByIdFormContent: React.FC<FindProductProps> = ({
  searchBy,
}) => {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [error, setError] = useState<string | null>(null);

  const onFinish = async (values: { id?: number; name?: string }) => {
    let value = null;
    let path = null;
    if (searchBy === "id") {
      value = values.id;
      path = "/api/products/api/products/";
    } else {
      value = values.name;
      path = "/api/products/api/products/name/";
    }
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
          setError("404 Error: Product not found");
        }

        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log(values);
      console.log(value);
      navigate(`/products/${data.id}/show`, {
        state: { product: data },
      });
    } catch (error) {
      console.error("Error during getting products: ", error);
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

          {searchBy === "id" ? (
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
          ) : (
            <Form.Item
              name="name"
              rules={[{ required: true, message: "Please input name!" }]}
            >
              <Input
                style={{ width: "100%" }}
                prefix={<UserOutlined className="site-form-item-icon" />}
                placeholder="Name"
              />
            </Form.Item>
          )}

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

export default FindProductByIdFormContent;
