# Frontend Design System

## Foundation

Phase 2 establishes centralized visual tokens in `frontend/src/styles.css`.

- Primary navigation: deep navy.
- Confirmed success: restrained green.
- Interactive accent: teal.
- Surface: white.
- Page background: light neutral.
- Border radius: 6px for controls, 8px for panels.
- Typography: DM Sans with Manrope display headings.

## Components

Reusable Phase 2 components live under `frontend/src/components/ui`:

- Button
- Field
- Avatar
- StatusBadge

Navigation components live under `frontend/src/components/navigation`:

- Sidebar
- TopBar

## Responsive Behavior

- Desktop uses a fixed application sidebar and top navigation.
- Mobile replaces the sidebar with a drawer opened by the menu control.
- Authentication switches to a stacked layout on small screens.
- Tables, summaries, and create forms stack at narrow widths.

## Data Rules

The frontend only renders domain data returned by backend REST APIs. It does not calculate financial totals, infer workspace authorization, or create production demo records.
