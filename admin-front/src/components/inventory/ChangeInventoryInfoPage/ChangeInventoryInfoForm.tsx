import React from "react";
import { Flex, Form, Layout } from "antd";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import InventoryInfoForm from "../generalComponents/InventoryInfoForm";

const ChangeInventoryInfoForm: React.FC = () => {
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const location = useLocation();
  const { state } = location;
  const value = state?.value;

  const onFinish = (values: any) => {
    interface ErrorField {
      name: string;
      errors: string[];
    }
    axios
      .put("/api/inventory/api/inventory/" + value, values, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      })
      .then(() => {
        form.resetFields();
        navigate("/inventory/menu")
      })
      .catch((error) => {
        if (error.response?.data) {
          const errorData = error.response.data;

          const errorFields: ErrorField[] = Object.entries(errorData).map(
            ([field, error]) => ({
              name: field,
              errors: Array.isArray(error) ? error : [error],
            })
          );
          console.log(errorFields);

          form.setFields(errorFields);
        }
      });
  };

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
      >
        <InventoryInfoForm
          form={form}
          onFinish={onFinish}
          buttonText="Change info"
        />
      </Flex>
    </Layout>
  );
};

export default ChangeInventoryInfoForm;
