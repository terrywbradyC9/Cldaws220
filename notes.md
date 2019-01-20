## DynamoDB Notes

aws dynamodb describe-table --table-name WordCountCache
aws dynamodb put-item --table-name WordCountCache --item '{"url": {"S": "foo"}}'   
aws dynamodb get-item --table-name WordCountCache --key file://key.temp

```
{"url": {"S":"foo"}}
```

## DynamoDB Policy

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "dynamodb:PutItem",
                "dynamodb:DescribeTable",
                "dynamodb:GetItem"
            ],
            "Resource": "arn:aws:dynamodb:us-west-2:----------:table/WordCountCache"
        }
    ]
}
```
