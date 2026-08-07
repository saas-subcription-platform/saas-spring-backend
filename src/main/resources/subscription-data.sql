-------------------------------------------------------
-- SUBSCRIPTION PLANS
-------------------------------------------------------

INSERT INTO subscription_plans
(
    sub_plan_id,
    plan_name,
    plan_description,
    maximum_users,
    active,
    created_at,
    updated_at
)
VALUES
    (
        1,
        'Starter',
        'Perfect for startups and small teams.',
        10,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        2,
        'Professional',
        'Ideal for growing organizations with collaboration features.',
        50,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        3,
        'Enterprise',
        'Best suited for large organizations with complete access.',
        500,
        TRUE,
        NOW(),
        NOW()
    )
    ON CONFLICT (sub_plan_id) DO NOTHING;


-------------------------------------------------------
-- PLAN PRICING
-------------------------------------------------------

INSERT INTO plan_pricing
(
    plan_pricing_id,
    plan_id,
    billing_cycle,
    price,
    active,
    created_at,
    updated_at
)
VALUES
    (
        1,
        1,
        'MONTHLY',
        999.00,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        2,
        1,
        'YEARLY',
        9999.00,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        3,
        2,
        'MONTHLY',
        2999.00,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        4,
        2,
        'YEARLY',
        29999.00,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        5,
        3,
        'MONTHLY',
        9999.00,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        6,
        3,
        'YEARLY',
        99999.00,
        TRUE,
        NOW(),
        NOW()
    )
    ON CONFLICT (plan_pricing_id) DO NOTHING;


-------------------------------------------------------
-- PLAN FEATURES
-------------------------------------------------------

INSERT INTO plan_feature
(
    plan_feature_id,
    plan_id,
    feature_name,
    feature_description,
    enabled,
    created_at,
    updated_at
)
VALUES
    (
        1,
        1,
        'Timesheet',
        'Timesheet Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        2,
        1,
        'Leave Management',
        'Employee Leave Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        3,
        2,
        'Timesheet',
        'Timesheet Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        4,
        2,
        'Leave Management',
        'Employee Leave Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        5,
        2,
        'Team Collaboration',
        'Internal Team Collaboration',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        6,
        3,
        'Timesheet',
        'Timesheet Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        7,
        3,
        'Leave Management',
        'Employee Leave Management',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        8,
        3,
        'Team Collaboration',
        'Internal Team Collaboration',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        9,
        3,
        'Goals & OKRs',
        'Goals and OKR Management',
        TRUE,
        NOW(),
        NOW()
    )
    ON CONFLICT (plan_feature_id) DO NOTHING;


-------------------------------------------------------
-- RESET POSTGRES SEQUENCES
-------------------------------------------------------

SELECT setval(
               pg_get_serial_sequence('subscription_plans', 'sub_plan_id'),
               COALESCE((SELECT MAX(sub_plan_id) FROM subscription_plans), 1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('plan_pricing', 'plan_pricing_id'),
               COALESCE((SELECT MAX(plan_pricing_id) FROM plan_pricing), 1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('plan_feature', 'plan_feature_id'),
               COALESCE((SELECT MAX(plan_feature_id) FROM plan_feature), 1),
               true
       );