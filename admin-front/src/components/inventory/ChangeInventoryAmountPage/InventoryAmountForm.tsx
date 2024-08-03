import { Button, Form, FormInstance, InputNumber } from "antd";
import { useEffect, useState } from "react";


interface AllInventoryAmountFormProps {
  form: FormInstance;
  onFinish: (values: any) => void;
  buttonText: string;
}

const InventoryAmountForm = ({
  form,
  onFinish,
  buttonText,
}: AllInventoryAmountFormProps) => {
  const [isButtonDisabled, setIsButtonDisabled] = useState<boolean>(true);

  useEffect(() => {
    const hasErrors = form.getFieldsError().some((field) => field.errors.length > 0);
    setIsButtonDisabled(hasErrors);
  }, [form]);

  const handleFieldsChange = () => {
    const hasErrors = form.getFieldsError().some((field) => field.errors.length > 0);
    setIsButtonDisabled(hasErrors);
  };
  
  return (
    <Form
      form={form}
      name="register"
      onFinish={onFinish}
      onFieldsChange={handleFieldsChange}
      initialValues={{
        residence: ["zhejiang", "hangzhou", "xihu"],
        prefix: "86",
      }}
      style={{ maxWidth: 600 }}
      scrollToFirstError
    >

      <Form.Item
        name="amount"
        rules={[
          {
            required: true,
            message: "Please input inventory amount!",
          },
          {
            type: "number",
            min: 0,
            max: 1000000,
            message: "Please enter a valid number!",
          },
        ]}
      >
        <InputNumber style={{width: "100%"}} placeholder="Inventory Amount" />
      </Form.Item>

      <Form.Item style={{ margin: "0%" }}>
        <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
          {buttonText}
        </Button>
      </Form.Item>
    </Form>
  );
};

export default InventoryAmountForm;
